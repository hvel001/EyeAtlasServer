/*
    1. Open psql
    2. \i [PATH.TO.SCRIPT]
*/

\c eye_atlas

DROP TABLE condition_tag;
DROP TABLE image;
DROP TABLE tags;
DROP TABLE condition;
DROP TABLE category;


\c postgres

DROP DATABASE eye_atlas;
