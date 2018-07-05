# generator-android-jhi [![NPM version][npm-image]][npm-url] [![Build Status][travis-image]][travis-url] [![Dependency Status][daviddm-image]][daviddm-url]
> Scaffolds Android Kotlin-Based App that consumes an Api scaffolded with [JHipster](https://jhipster.tech) generator.

Currently this generator is in experimental fase and works with Monolithic Application, and JWT authentication. To use the social login you have to modify a little bit your Api project

## Installation

First, install [Yeoman](http://yeoman.io) and generator-android-jhi using [npm](https://www.npmjs.com/) (we assume you have pre-installed [node.js](https://nodejs.org/)).

```bash
npm install -g yo
npm install -g generator-android-jhi
```

## Usage

Create a folder and go inside

```bash
mkdir my-android-project && cd my-android-project
```

then generate the code:

```bash
yo android-jhi
```

You can copy your .jhipster folder from the target Jhipster project and run:

```bash
yo android-jhi:entity
```

Then you can choose which entities do you want to consume with your mobile App. CRUD operations will be scaffolded 

Finally import the project with Android Studio

## Configure Social Login

On your web/api project use the [jhipster-social-login-api](https://github.com/greengrowapps/generator-jhipster-social-login-api) to add the extra controllers that you need and follow the instructions

For google, run 
```bash
./gradlew signingReport
```

And generate as many Android credentials as you need (one for packageName-keystoreSHA1 pair)

then copy the Client-id from YOUR WEB CREDENTIAL (don't use the client-id from the Android credentials) and replace the value of the "google_login_web_client_id" string resource

For facebook replace the two string resources for de values in your credential

## Getting To Know Yeoman

 * Yeoman has a heart of gold.
 * Yeoman is a person with feelings and opinions, but is very easy to work with.
 * Yeoman can be too opinionated at times but is easily convinced not to be.
 * Feel free to [learn more about Yeoman](http://yeoman.io/).

## License

Apache-2.0 © [Green grow apps](https://www.greengrowapps.com)


[npm-image]: https://badge.fury.io/js/generator-android-jhi.svg
[npm-url]: https://npmjs.org/package/generator-android-jhi
[travis-image]: https://travis-ci.org/greengrowapps/generator-android-jhi.svg?branch=master
[travis-url]: https://travis-ci.org/greengrowapps/generator-android-jhi
[daviddm-image]: https://david-dm.org/greengrowapps/generator-android-jhi.svg?theme=shields.io
[daviddm-url]: https://david-dm.org/greengrowapps/generator-android-jhi
