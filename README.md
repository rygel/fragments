Lyricist
=========
[![Maven Central](http://img.shields.io/maven-central/v/io.andromeda/fragments.svg)](http://search.maven.org/#search|ga|1|io.andromeda)

A blog/static pages engine for the [Pippo](https://github.com/decebals/pippo) Java Micro Framework. It is inspired by
 [Poet](http://jsantell.github.io/poet/). It supports the following features:
- [Author Pages](fragments-demo/README.md#author_pages)
- [Static Pages](fragments-demo/README.md#static_pages)
- [Blog and Post Level Context](fragments-demo/README.md#context)
- Categories
- Drafts
- Multiple Blogs
- Tags
- JSON/YAML Front Matter
- Valid until management (not yet implemented)
- A Maven Quickstart Archetype (not yet implemented)

## Use Cases
Lyricist is useful for two main use cases:
1. As a blog (obviously).
2. As a semi-static site engine for websites. E.g. you can use Lyricist to hold/render your about, disclaimer, agb pages.

## How to use
Configure the data directories in the Pippo application.properties file.
```
# Lyricist
lyricist.blogs = rootBlog:root, blog:blog
```
Use the key "lyricist.blogs" for a list of blogs.
Each key inside the list consists of a pair of [blog name]:[blog main directory]. In this example there is a blog called
"rootBlog" and it uses the main directory "root", which translates to `resources/lyricist/root/`.

In each data directory there are two subdirectories: `authors` and `posts`. In `posts` there are all posts 
located. In `authors` are files for all authors of the blog located. Both the posts and authors are Markdown 
formatted with a YAML header. The authors directory is optional. If it doesn't exist or contains no files the author 
support is disabled for this blog.

```
---
layout: post
title: First Post
slug: first-post
tags: [blog, fun]
category: [java, web]
published: 2015-07-01T01:02:03
validUntil: 2015-08-01T00:00:00
draft: false
authors: [admin, rygel]
context: 
  pageWide
---
This is the first posting using Lyricist.
```