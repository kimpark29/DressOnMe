import os
import numpy as np
import matplotlib.pyplot as plt
import cv2
import tensorflow as tf
from PIL import Image
# from flask import Flask, request, jsonify

def get_img(imgpath):
    img = cv2.imread(imgpath)
    img = cv2.resize(img, (256, 256))

    return img


def predict(img, model):
    img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    img_proc = [img_gray / 255]
    img_proc = np.array(img_proc)

    predictions = model.predict(img_proc)
    cv2.imwrite('result/predict.png', predictions[0])

    return predictions[0]


def get_clothes(actual, mask, min_color, max_color):
    mask = cv2.cvtColor(mask, cv2.COLOR_RGB2BGR)
    mask = mask * 255
    mask = mask.astype(int)
    cv2.imwrite('result/image.png', mask)
    im_mask = cv2.imread('result/image.png')
    im_mask = cv2.cvtColor(im_mask, cv2.COLOR_BGR2RGB)
    masking = cv2.inRange(im_mask, min_color, max_color)
    result = cv2.bitwise_and(actual, actual, mask=masking)

    return result


def transparant_img(imgpath, new_path, img):
    im = cv2.imread(imgpath)
    size = im.shape

    cv2.imwrite(new_path + "dummy.png", img)

    img = Image.open(new_path + "dummy.png")
    img = img.convert("RGBA")

    els = img.getdata()

    new_els = []

    for el in els:
        if el[0] == 0 and el[1] == 0 and el[2] == 0:
            new_els.append((255, 255, 255, 0))
        else:
            new_els.append(el)

    img.putdata(new_els)
    image = img.resize((size[1], size[0]), resample=0)
    image.save(new_path + "dummy5.png", "PNG")
    print("Done")

    return image


#app = Flask(__name__)


#@app.route("/", methods=["GET", "POST"])
def index():

    # Put the Input of Image HERE
    path = "image_sample/0021.jpg"
    model_path = 'mymodel/model_001.h5'

    #model = load_model(model_path)
    model = tf.keras.models.load_model(model_path)

    min_color = (165 - 50, 218 - 50, 53 - 50)
    max_color = (255, 255, 100)
    img = get_img(path)
    mask = predict(img, model)

    clothes = get_clothes(img, mask, min_color, max_color)
    result = transparant_img(path, "result/", clothes)

    return result


if __name__ == "__main__":
    #app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
    index()
