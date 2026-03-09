### i18n
.properties doesn't use UTF-8 by default, so we need to specify it explicitly. 
Otherwise, non-ASCII characters will be garbled.
To use UTF-8 encoding go to Settings → Editor → File Encodings and set "Default encoding for properties files" to UTF-8.


### git remove from indexing
git rm --cached settings.local.json

###
GET http://localhost:8080/api/tasks


###
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
"details": "Третья задача"
}

###
POST http://localhost:8080/api/tasks
Content-Type: application/json
Accept-Language: ru-RU
{
"details": null
}
