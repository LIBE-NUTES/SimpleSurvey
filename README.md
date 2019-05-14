
# Simple Survey
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)

Library for simple survey creation.
This library has as main dependency the library [AppIntro](https://github.com/AppIntro/AppIntro) _(An excellent library for you to create introductions to your application)_.

**Simple Survey**, only provides an easy way to create forms with pre-defined question types.

## Features
- Support for the following question types:
	- Dichotomic choice (e.g. example YES/NO)
	- Single choice
	- Multiple choose
	- Open question
	- Date choice
	- Time choice
- Presentation page
- Question blocked until response is obtained.

## Installation
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```
dependencies {
	 implementation 'com.github.nutes-uepb:simple-survey:v1.2.1'
}
```

## Using

#### [See example of use](https://github.com/nutes-uepb/simple-survey/blob/master/app/src/main/java/br/edu/uepb/nutes/simplesurvey/SimpleSurvey1.java)

`More examples coming soon...`


