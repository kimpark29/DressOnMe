import os
import numpy as np
import cv2
import tensorflow as tf
from PIL import Image
import requests
import json
import urllib.request
from flask import Flask


# LOGIN TO GET TOKEN FROM API
def login():
    url_login = "https://dressonmeapi-clh64ny43a-et.a.run.app/api/user/login"

    resp = requests.post(url_login, json={"email": "ml@dressonme.com", "password": "dressonme"})
    tk = json.loads(resp.text)['authToken']

    return tk


# GET THE IMAGE DATA TO BE PREDICT
def process(tk):
    url_process = "https://dressonmeapi-clh64ny43a-et.a.run.app/api/process"
    headers = {"auth-token": str(tk)}

    resp = requests.get(url_process, headers=headers)
    data = json.loads(resp.text)

    proc = data['process']

    user_id = []
    img_data = []

    for i in range(len(proc)):
        if proc[i]['status'] == True:
            user_id.append(proc[i]['_id'])
            img_data.append(proc[i]['linkModel'])

    return user_id, img_data


def reset_directory(dirpath):
    for root, dirs, files in os.walk(dirpath):
        for file in files:
            os.remove(os.path.join(root, file))
    print("deleted!")


def get_img(img_data):
    img_list = []
    x = []
    y = []
    for img_url in img_data:
        req = urllib.request.urlopen(img_url)
        arr_url = np.asarray(bytearray(req.read()), dtype=np.uint8)
        img = cv2.imdecode(arr_url, 1)
        cv2.imwrite('temp/temp_img.png', img)
        img = cv2.imread('temp/temp_img.png')

        xa, ya, _ = img.shape
        img = cv2.resize(img, (256, 256))
        img_list.append(img)
        x.append(xa)
        y.append(ya)

    return img_list, x, y


def predict(img_list, model):
    temp_img = []
    i = 0
    for img in img_list:
        img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        img_normal = img_rgb / 255
        temp_img.append(img_normal)
        i += 1

    img_proc = np.array(temp_img)

    predictions = model.predict(img_proc)
    for i in range(len(predictions)):
        predictions[i] = cv2.merge((predictions[i, :, :, 0], predictions[i, :, :, 1], predictions[i, :, :, 2]))
    return predictions


def get_clothes(img_list, mask_list, min_color, max_color, x, y):
    temp_img = []
    for i in range(len(mask_list)):
        # GET THE FASHION FROM THE IMAGE
        mask = cv2.cvtColor(mask_list[i], cv2.COLOR_RGB2BGR)
        mask = mask * 255
        mask = mask.astype(int)
        cv2.imwrite('temp/temp_mask.png', mask)
        im_mask = cv2.imread('temp/temp_mask.png')
        im_mask = cv2.cvtColor(im_mask, cv2.COLOR_BGR2RGB)
        masking = cv2.inRange(im_mask, min_color, max_color)
        result = cv2.bitwise_and(img_list[i], img_list[i], mask=masking)

        # TRANSPARANTING THE IMG
        result = cv2.resize(result, (y[i], x[i]))
        cv2.imwrite("temp/temp_untransparant.png", result)

        img = Image.open("temp/temp_untransparant.png")
        img = img.convert("RGBA")

        els = img.getdata()

        new_els = []

        for el in els:
            if el[0] == 0 and el[1] == 0 and el[2] == 0:
                new_els.append((255, 255, 255, 0))
            else:
                new_els.append(el)

        img.putdata(new_els)
        img.save("result/" + str(i) + ".png", "PNG")
        temp_img.append(str(i) + ".png")

    return temp_img


def patch_image(img_name, id, tk):
    headers = {"auth-token": str(tk)}

    for i in range(len(id)):
        url = 'https://dressonmeapi-clh64ny43a-et.a.run.app/api/process/' + id[i] + '/filtering'
        path_img = 'result/' + img_name[i]

        files = {'linkFiltering': (os.path.basename(path_img), open(path_img, 'rb'), "image/png")}

        r2 = requests.patch(url, headers=headers, files=files)

    return r2


app = Flask(__name__)

@app.route("/", methods=["GET", "POST", "PATCH"])
def index():
    try:
        # GET THE DATA FROM API
        tk = login()
        id, img_data = process(tk)

        print(id)
        print(img_data)

        # PROCESS THE DATA AND PREDICT THE IMAGE THEN SEND IT
        reset_directory('result')

        model_path = 'mymodel/model_001.h5'

        model = tf.keras.models.load_model(model_path)
        print("model has loaded")
        min_color = (165 - 50, 218 - 50, 53 - 50)
        max_color = (255, 255, 100)

        # GET THE IMAGE FROM THE LIST
        img_list, x, y = get_img(img_data)

        if img_list == []:
            print("No images found")
        else:
            print("images has been loaded")

        # PREDICT THE IMAGE
        mask = predict(img_list, model)
        print("image has been predicted")
        result_data = get_clothes(img_list, mask, min_color, max_color, x, y)
        print("image has been transparanted")

        print(result_data)

        patch_image(result_data, id, tk)

        print("image has been patch")

        return "OK"

    except Exception as e:
        error = "No Data Found!"
        return error


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 3000)))
