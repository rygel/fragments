TODO
====

Add db (h2) to save access of fragments

## Database tables

Table name: FRAGMENT_NAME_statistics

| Name   | Type            | Description                           |
|:-------|:----------------|:--------------------------------------|
| id     | INT PRIMARY KEY | ID of the entry                       |
| name   | VARCHAR(1000)   | The filename of the fragment          |
| clicks | BIGINT          | The number of clicks of this fragment |
