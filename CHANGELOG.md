## Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

### [unreleased]

#### Fixed

#### Changed
- Updated h2database to 1.4.197.

#### Added

#### Removed


### [0.0.22] 2018-10-30

#### Fixed
- Fixed date parsing for RouteType.Blog.
- Fix NPE in prepareFragments (#1).
- Correctly setup flexmark for processing Markdown tables.

#### Changed
- Updated flexmark to 0.32.56.

#### Added

#### Removed

### [0.0.21] 2018-04-05

#### Fixed

#### Changed
- Updated flexmark to 0.32.12
- Updated slf4j to 1.7.25
- Added database support. Currently for embedded H2 database only.
- Added preliminary full-text feeds support via Apachae Lucene.

#### Added

#### Removed

### [0.0.20] 2018-02-24

#### Fixed

#### Changed

#### Added
- Added dynamic context functionality

### [0.0.18] 2017-03-12

#### Fixed

#### Changed

#### Added
- Added name property to fragment.
- Added preview_text_only to fragment.

#### Removed

### [0.0.16] 2017-03-

#### Fixed

#### Changed
- Configured SnakeYAML to not implicitly convert the data types. This allows for the same behavior for YAML and JSON front matter.

#### Added
- Configuration.registerOverviewRoute

#### Removed

### [0.0.12] 2017-01-24

#### Fixed
- Bug fixes

#### Changed

#### Added

#### Removed
