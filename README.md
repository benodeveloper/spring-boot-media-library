# Spring boot Media Library
The Spring boot media library simplifies file uploading, file processing and content optimization

## Image Processing API
Scalable image REST API to resize, crop, convert, edit, and optimize images in real-time.

```shell
GET http://localhost:8080/api/images/example.jpg?w=800&h=800
```

### 1 - Upload your input
Firstly, your image must be uploaded or accessible.

Upload an image with a simple HTTP request:
```shell
POST http://localhost:8080/api/images/upload
--------------------------------------------
curl --request POST \
  --url http://localhost:8080/api/images/upload \
  --header 'Content-Type: multipart/form-data' \
  --form 'image=@example.jpg' # File must exist locally. Always include the '@'.
```

````json 'Response Body'
{
  "file": {
    "file_path" : "/uploads/images/example.jpg",
    "file_url": "http://localhost:8080/api/image/example.jpg"
  }
}
````