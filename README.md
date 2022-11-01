Java test assignment (driver management microservice)

After some consideration, I decided to use Kotlin instead of Java. If needed, the project can be rewritten using Java.

The microservice was created in IntelliJ IDEA with Spring, using PostGIS Docker image for database management and Swagger UI tj unterface with endpoints.
Commands to pull postgis and swagger via docker:

docker pull postgis/postgis
docker pull swaggerapi/swagger-ui

Docker PostGIS container can be created and started with:

docker run -d -p 5432:5432 --name driver -e POSTGRES_PASSWORD=driver postgis/postgis

Datasource URL, username and password (postgres, driver) are specified in src/main/resources/application.properties
The PostGIS extension apparently uses up Postgres image feature for running scripts on startup, so sample database can be created by copying and executing the script manully:

docker cp ./init/init.sql driver:/docker-entrypoint-initdb.d/init.sql
docker exec -u postgres driver psql postgres postgres -f docker-entrypoint-initdb.d/init.sql

The database contains three tables:

driver(
    driver_id serial PRIMARY KEY,
    driver_name varchar,
    driver_surname varchar,
    driver_birthday timestamptz,
    driver_address text,
    driver_position geometry)

shift(
    shift_id serial PRIMARY KEY,
    driver_id int references driver(driver_id) ON DELETE CASCADE,
    shift_start timestamptz,
    shift_end timestamptz)

boundary(
    boundary_id serial PRIMARY KEY,
    boundary_name varchar,
    boundary_geometry geometry)

Driver and shift tables contain sample data inserted by init.sql, while boundaries table is empty (see below)
When the application is run, Swagger UI can be accessed at:

http://localhost:8080/swagger-ui/index.html

Shift controller:

/shifts/add - adds a shift to the database. Requires driver_id, shift start and end timestamps (UNIX milliseconds are used), time is assumed to be UTC
curl -X 'POST' \
  'http://localhost:8080/shifts/add?driverId=3&shiftStart=1667246400000&shiftEnd=1667275200000' \
  -H 'accept: */*' \
  -d ''

/shifts/list - returns list of existing shifts from the database
curl -X 'GET' \
  'http://localhost:8080/shifts/list' \
  -H 'accept: */*'

/shifts/contain - returns a list of shifts containing the provided time interval with corresponding driver data, requires interval start and end timestaps
curl -X 'GET' \
  'http://localhost:8080/shifts/contain?intervalStart=1667203200000&intervalEnd=1667210400000' \
  -H 'accept: */*'

Driver controller:

/drivers/updateposition - updates position of the specified driver (id) with the provided coordinates and srid (4326 is used in the sample DB)
curl -X 'POST' \
  'http://localhost:8080/drivers/updateposition?driverId=3&lat=34.78581453179752&lon=31.255739540165827&srid=4326' \
  -H 'accept: */*' \
  -d ''

/drivers/add - adds a driver to the database (position has to be PostGIS geometry string, I didn't have the time to change it unfortunately)
curl -X 'POST' \
  'http://localhost:8080/drivers/add?name=Kim&surname=Kitsuragi&birthday=478137600000&address=---&position=SRID%3D4326%3BPOINT%2831.255739540165827%2034.78581453179752%29' \
  -H 'accept: */*' \
  -d ''

/drivers/delete - deletes the driver specified by id from the database
curl -X 'DELETE' \
  'http://localhost:8080/drivers/delete?id=4' \
  -H 'accept: */*'

/drivers/list - returns a list of drivers in the database
curl -X 'GET' \
  'http://localhost:8080/drivers/list' \
  -H 'accept: */*'

Boundary controller:

/boundary/import - imports geodata from geojson file and adds each feature to the database separately. Project directory contains a sample geojson file for import
curl -X 'POST' \
  'http://localhost:8080/boundaries/import?fileName=geojson%2Fisrael_ad.json' \
  -H 'accept: */*' \
  -d ''

/boundary/add - adds a boundary to the database (geometry in geojson format)
curl -X 'POST' \
  'http://localhost:8080/boundaries/add?name=example&geometry=%7B%22type%22%3A%22Polygon%22%2C%22coordinates%22%3A%5B%5B%5B30%2C30%5D%2C%5B30%2C31%5D%2C%5B31%2C31%5D%2C%5B31%2C30%5D%2C%5B30%2C30%5D%5D%5D%7D' \
  -H 'accept: */*' \
  -d ''

/boundary/list - returns a list of boundaries in the database
curl -X 'GET' \
  'http://localhost:8080/boundaries/list' \
  -H 'accept: */*'

/boundary/driversinboundary - returns a list if drivers located within specified boundary
curl -X 'GET' \
  'http://localhost:8080/boundaries/driversinboundary?boundaryId=4' \
  -H 'accept: */*'