curl -X POST \
http://${host}/login \
-H 'Content-Type: application/json' \
-H 'cache-control: no-cache' \
-d '${dataBody}'