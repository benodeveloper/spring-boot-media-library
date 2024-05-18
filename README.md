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
  "file_url": "http://localhost:8080/api/images/f16367c4-156c-40f0-bfd7-a3826b7554fd/example.jpg",
  "file_path": "uploads/f16367c4-156c-40f0-bfd7-a3826b7554fd/example.jpg",
  "filename": "example.jpg",
  "contentType": "image/jpeg",
  "size": 446272 // Bytes
}
````