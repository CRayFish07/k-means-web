###KMeans calculator

Please run with `java -jar kMeansWeb-1.0-SNAPSHOT.jar`

Application by default connects to MySQL database with `jdbc:mysql://localhost:3306/kmeans`

This can be changed by running binary file with 

`java -Dspring.datasource.url=jdbc:mysql://address:port/dbname -jar kMeansWeb-1.0-SNAPSHOT
.jar`

Application runs on port `8080`.

Database scheme is:
```
CREATE TABLE kmcluster
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL
);
CREATE TABLE kmcluster_points
(
    kmcluster_id BIGINT NOT NULL,
    points_id BIGINT NOT NULL
);
CREATE TABLE kmpoint
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL
);
CREATE TABLE kmresult
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT
);
CREATE TABLE kmresult_clusters
(
    kmresult_id BIGINT NOT NULL,
    clusters_id BIGINT NOT NULL
);
ALTER TABLE kmcluster_points ADD FOREIGN KEY (kmcluster_id) REFERENCES kmcluster (id);
ALTER TABLE kmcluster_points ADD FOREIGN KEY (points_id) REFERENCES kmpoint (id);
CREATE UNIQUE INDEX UK_dxoe21kbmett3j9g6a6x7krfj ON kmcluster_points (points_id);
CREATE INDEX FK_8gxf2iellj2bsulursvlv1au9 ON kmcluster_points (kmcluster_id);
ALTER TABLE kmresult_clusters ADD FOREIGN KEY (clusters_id) REFERENCES kmcluster (id);
ALTER TABLE kmresult_clusters ADD FOREIGN KEY (kmresult_id) REFERENCES kmresult (id);
CREATE UNIQUE INDEX UK_29laywyi61j25g3x2j9c8f3qp ON kmresult_clusters (clusters_id);
CREATE INDEX FK_d8r8tcjfa56baw9heimvfhtgp ON kmresult_clusters (kmresult_id);
```