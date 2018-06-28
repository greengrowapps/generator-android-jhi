'use strict';
const Generator = require('yeoman-generator');
const chalk = require('chalk');
const yosay = require('yosay');

module.exports = class extends Generator {
  prompting() {
    // Have Yeoman greet the user.
    this.log(yosay(`Welcome to the primo ${chalk.red('android-jhi')} generator!`));

    const prompts = [
      {
        type: 'input',
        name: 'appName',
        message: 'Type the App name',
        default: 'My App'
      },
      {
        type: 'input',
        name: 'packageName',
        message: 'Type the app Package name',
        default: 'com.company.app'
      },
      {
        type: 'confirm',
        name: 'googleLogin',
        message: 'Do you want social login with Google?',
        default: true
      },
      {
        type: 'confirm',
        name: 'facebookLogin',
        message: 'Do you want social login with Facebook?',
        default: true
      }
    ];

    return this.prompt(prompts).then(props => {
      // To access props later use this.props.someAnswer;
      this.props = props;
    });
  }

  writing() {
    const packageDir = this.props.packageName.replace(/\./g, '/');
    const oldPackageDir = 'com/greengrowapps/mycountersapp';

    /**
     * The files to copy
     * @type {[[string]]}
     */
    const copyFiles = [
      ['gradle'],
      ['gradlew'],
      ['gradlew.bat'],
      [`app/src/main/res/drawable`],
      [`app/src/main/res/drawable-v24`],
      [`app/src/main/res/drawable-hdpi`],
      [`app/src/main/res/drawable-mdpi`],
      [`app/src/main/res/drawable-xhdpi`],
      [`app/src/main/res/drawable-xxhdpi`],
      [`app/src/main/res/drawable-xxxhdpi`],
      [`app/src/main/res/mipmap-anydpi-v26`],
      [`app/src/main/res/mipmap-hdpi`],
      [`app/src/main/res/mipmap-mdpi`],
      [`app/src/main/res/mipmap-xhdpi`],
      [`app/src/main/res/mipmap-xxhdpi`],
      [`app/src/main/res/mipmap-xxxhdpi`]
    ];

    copyFiles.forEach(([src, dest = src]) => {
      this.fs.copy(`${this.sourceRoot()}/${src}`, `${dest}`, this.props);
    });

    /**
     * The files to template
     * @type {[[string]]}
     */
    const templateFiles = [
      ['gradle.properties'],
      ['settings.gradle'],
      ['.gitignore'],
      ['build.gradle'],
      ['app/build.gradle'],
      ['jhiusers/build.gradle'],
      ['app/src/main/androidManifest.xml'],
      [`app/src/main/java/${oldPackageDir}`, `app/src/main/java/${packageDir}`],
      [`app/src/test/java/${oldPackageDir}`, `app/src/test/java/${packageDir}`],
      ['jhiusers/src/main/androidManifest.xml'],
      [
        'jhiusers/src/main/java/com/greengrowapps/jhiusers',
        'jhiusers/src/main/java/com/greengrowapps/jhiusers'
      ],
      ['app/.gitignore', 'app/.gitignore'],
      ['app/proguard-rules.pro'],
      [`app/src/main/res/menu`],
      [`app/src/main/res/layout`],
      [`app/src/main/res/values`],
      [`app/src/main/res/values-v21`]
    ];

    templateFiles.forEach(([src, dest = src]) => {
      this.fs.copyTpl(
        `${this.sourceRoot()}/${src}`,
        `${dest}`,
        this.props
      );
    });
  }

  install() {
    //this.installDependencies();
  }
};
