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


- Implement tags and categories
- Implement archive support
Put the Fragments into maps
    ArchiveYear
    List<Fragment> january
    List<Fragment> february

    ArchiveMonth
    List<Day, List<Fragment>>

Map<Year,Map<Month,Map<Day,List<Fragment>>>>
