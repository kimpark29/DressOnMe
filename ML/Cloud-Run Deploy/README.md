gcloud builds submit --tag gcr.io/dressonmi/index

gcloud run deploy --image gcr.io/dressonmi/index --platform managed