This application is the backend part of the web service for gift certificates.

The application is created using technologies and tools such as Gradle, Spring(Data, Security), Spring Boot, Hibernate, SQL, REST.
MySQL is used for data storage. The database stores data of entities such as gift certificate, tag, user, order.

Opportunities of the application.

1. CRUD operations for gift certificates data.
2. CRD operations for users data.
3. Create and read operations for orders data.
4. Read operation for tags data.
5. Search data by parameters, sorting and pagination of data.

This application assumes three roles:
1. guest:
- authentication is not required;
- can read gift certificates data;
- can log in;
- can register;
2. user:
- authentication is required;
- can read gift certificates data;
- can read own user data;
- can create order and read data of own orders;
3. admin:
- authentication is required;
- can create, read, update and delete gift certificates data;
- can read users data, add admin status to other users, delete user;
- can create orders and read orders data.