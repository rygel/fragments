Fragments
=========
[![Maven Central](http://img.shields.io/maven-central/v/io.andromeda/fragments.svg)](http://search.maven.org/#search|ga|1|io.andromeda)

A pages collection engine for the [Pippo](https://github.com/decebals/pippo) Java Micro Framework. It is loosely inspired by
 [Poet](http://jsantell.github.io/poet/).

The main features are:
----------------------
- Markdown files with JSON or YAML Front Matter
- Global or local (per file) template definitions
- Global and local (per file) Context
- Invisible Pages (drafts)
- Expiring files (not yet implemented)

## Use Cases
I have often the case that I want to display collections of data with Pippo, e.g. a list of articles, a list of events, etc.
For this I have created Fragmments. It holds all markdown files and creates routes for them in Pippo.

## How to use
Configure fragments.
```
Fragments fragments = new Fragments(this,"Events", "/posts/", currentPath + "/data/posts", "posts_overview", "post", null);
```

Example of an Markdown file.

```
---
template: event
title: My First Event
slug: first-event

isvisible: false
---
This is the first event using Fragments.
```
