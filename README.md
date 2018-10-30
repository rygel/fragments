Fragments
=========
[![Build Status](https://travis-ci.org/rygel/fragments.svg?branch=master)](https://travis-ci.org/rygel/fragments)
[![Coverage Status](https://coveralls.io/repos/github/rygel/fragments/badge.svg?branch=master)](https://coveralls.io/github/rygel/fragments?branch=master)
[![Maven Central](http://img.shields.io/maven-central/v/io.andromeda/fragments.svg)](http://search.maven.org/#search|ga|1|io.andromeda)
[![Javadocs](http://www.javadoc.io/badge/io.andromeda/fragments.svg)](http://www.javadoc.io/doc/io.andromeda/fragments)

A pages collection engine for the [Pippo](https://github.com/pippo-java/pippo) Java Micro Framework. It is loosely inspired by [Poet](http://jsantell.github.io/poet/).

The main features are:
----------------------

- Markdown files with JSON or YAML Front Matter
- Global or local (per file) template definitions
- Global and local (per file) Context
- Invisible Pages (e.g. drafts)
- Generation of preview texts
- Support of multiple languages
- Categories and Tags

### Todo

- Expiring files
- Save the access statistics of the Fragments into a DB

## Other Features

- Supports obfuscation of email addresses via [flexmark-java](https://github.com/vsch/flexmark-java) (in the Markdown files) as well as directly via the default context.
- Has a dynamic context, which is used to dynamically inject information during each call to a route.

## Use Cases
I have often the case that I want to display collections of data with Pippo, e.g. a list of articles, a list of events, etc.
For this I have created Fragments. It holds all markdown files and creates routes for them in Pippo.

## How to use
Configure fragments.
```java
 Fragments froot = new Fragments(this, "Root", "/", currentPath + "/data/fragments/root", "", "static_page", defaultContext, configuration);
```

Example of an Markdown file.

```
---
title    : My First Event
slug     : first-event
template : event
date     : 2017-01-12T12:12
preview  : 
order    : 
visible  : false
---
This is the first event using Fragments.
```

###  Fragment properties
| Property         | Type    | Default value           | Description                                                                                                                                                                                                                       |
|:-----------------|:--------|:------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| content          | String  | ""                      | The textual content of the fragment. If the fragment contains multiple languages, it contains the content of the current language.                                                                                                |
| date             | String  | ""                      | The date(time) of the Fragment. It's pattern is "yyyy-MM-dd['T'HH:mm]". So either 2018-01-02 or 2018-01-02T10:11.                                                                                                                                                                                            |
| defaultLanguage  | String  | ""                      | The default language of the fragment.                                                                                                                                                                                             |
| fullUrl          | String  | The full URL.           | The full URL of the fragment, the concatenation of protocol, domain, fragments.baseURL and fragment.url.                                                                                                                          |
| fullUrlEncoded   | String  | The full encoded URL.   | The full URL of the fragment, the concatenation of protocol, domain, fragments.baseURL and frgment.url encoded to be used as a parameter for Facebook or Whatsapp sharing.                                                        |
| languages        | TreeMap |                         | The list of languages as defined in Pippo.                                                                                                                                                                                        |
| languagesPreview | TreeMap |                         | The list of all preview texts of the contents in the different languages.                                                                                                                                                         |
| languagesTitles  | TreeMap |                         | The list of all content texts in the different languages.                                                                                                                                                                         |
| name             | String  | The Fragments name      | The name of the Fragments class this Fragment belongs to.                                                                                                                                                                         |
| order            | int     | autoincrement           | The order of the fragments. Can be defined via front matter. If not defined via front matter, than it is autogenerated based on alphabetic ordering of the fragment's titles.                                                     |
| preview          | String  | ""                      | The preview of the textual content of the fragment. If the fragment contains multiple languages, it contains the preview of the current language.                                                                                 |
| previewTextOnly  | String  | ""                      | The preview of the textual content of the fragment (in the current language) without HTML tags.                                                                                                                                   |
| slug             | String  | The slug.               | The slug of the fragment. Either defined via the front matter or autogenerated.                                                                                                                                                   |
| template         | String  | via front matter        | The template for the fragment. Defined via front matter. Can overwrite the definition via Fragments on a per fragment basis.                                                                                                      |
| title            | String  | via front matter        | The title as defined via front matter. If the fragment contains multiple languages, it contains the title of the current language. To be implemented!                                                                             |
| url              | String  | The baseUrl + the slug. | The base URL + the slug of the fragment.                                                                                                                                                                                          |
| visible          | boolean | true                    | Defines if the fragment is visible or not. Default value in Java is false, which will automatically overwritten with true, if it is not defined in the front matter. For usability purposes it can be seen as defaulting to true. |

### Fragment preview marker

- preview tag inside of front matter
- ```<!--more-->``` tag inside the content. Can be written as either ```<!--more-->, <!-- more -->, <!-- more-->, <!--more -->```.

### Configuration class
Is used to configure the following properties of a Fragment:

| Property              | Type           | Default value      | Description                                                                                                                                                |
|:----------------------|:---------------|:-------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| domain                | String         | ""                 | The domain of the website, e.g. example.com                                                                                                                |
| dynamicContext        | DynamicContext | null               | Accepts a class implementing the DynamicContext interface. Is intented to be used to update the context during the execution of a program. Otherwise the context will be static over the runtime of the application. |
| extension             | String         | ".md"              | The extension of the markdown files.                                                                                                                       |
| protocol              | String         | "https://"         | The protocol of the website. Used for constructing the fully encoded URL.                                                                                  |
| registerOverviewRoute | boolean        | true               | When set to false the route for the baseURL will not be automatically be registered. This allows to create that route manually with total control.         |
| routeType             | RouteType      | RouteType.ARTICLES | The route type of the Fragments object. Defaults to ARTICLES, but can be changed to Blog, which will include the date into the URL, e.g. /2017/01/14/slug. |

### Dynamic Context
Rationale: Imagine you want to display the current date on the website. For this you have to include it in the context of the
route which is displayed. For routes created by Fragments you have to define a DynamicContext and add it to the Configuration class.
Then the Dynamic Context will always be executed once one of the routes of this fragment is requested.

### Example websites

- 


### Dependencies

- [pippo-core](https://github.com/pippo-java/pippo) to be able to automatically generate the routes.
- [flexmark-java](https://github.com/vsch/flexmark-java) as a markdown parser.
- [SnakeYaml](https://bitbucket.org/asomov/snakeyaml) as a YAML (front matter) parser.
- [Fast JSON](https://github.com/alibaba/fastjson) as a JSON (front matter) parser.
- [Apache Commons IO](https://commons.apache.org/proper/commons-io/) for convenience file name handling.
- [SLF4J](http://www.slf4j.org) as a logging adapter.
- [Logback](http://logback.qos.ch) as the logger implementation.
