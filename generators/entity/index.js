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
        name: 'packageName',
        message: 'Type the app Package name',
        default: 'com.company.app'
      },
      {
        type: 'input',
        name: 'entityName',
        message: 'Type the entity name',
        default: 'entity'
      }
    ];

    return this.prompt(prompts).then(props => {
      // To access props later use this.props.someAnswer;
      props.entityName =
        props.entityName.substr(0, 1).toUpperCase() +
        props.entityName.toLowerCase().substr(1);
      props.entityNameLower = props.entityName.toLowerCase();
      this.props = props;
    });
  }

  writing() {
    const packageDir = this.props.packageName.replace(/\./g, '/');
    const oldPackageDir = 'com/greengrowapps/myapp';

    const oldEntityDir = oldPackageDir + '/core/data/entity';
    const entityDir = packageDir + '/core/data/' + this.props.entityNameLower;

    /**
     * The files to template
     * @type {[[string]]}
     */
    const templateFiles = [
      [
        `app/src/main/java/${oldEntityDir}/EntityDto.kt`,
        `app/src/main/java/${entityDir}/${this.props.entityName}Dto.kt`
      ],
      [
        `app/src/main/java/${oldEntityDir}/EntityRestResource.kt`,
        `app/src/main/java/${entityDir}/${this.props.entityName}RestResource.kt`
      ],
      [
        `app/src/main/java/${oldEntityDir}/EntityService.kt`,
        `app/src/main/java/${entityDir}/${this.props.entityName}Service.kt`
      ],
      [
        `app/src/main/java/${oldPackageDir}/viewadapters/EntityViewAdapter.kt`,
        `app/src/main/java/${packageDir}/viewadapters/${
          this.props.entityName
        }ViewAdapter.kt`
      ],
      [
        `app/src/main/java/${oldPackageDir}/EntityActivity.kt`,
        `app/src/main/java/${packageDir}/${this.props.entityName}Activity.kt`
      ],
      [
        `app/src/main/res/layout/activity_entity.xml`,
        `app/src/main/res/layout/activity_${this.props.entityNameLower}.xml`
      ],
      [
        `app/src/main/res/layout/content_entity.xml`,
        `app/src/main/res/layout/content_${this.props.entityNameLower}.xml`
      ],
      [
        `app/src/main/res/layout/view_entity_item.xml`,
        `app/src/main/res/layout/view_${this.props.entityNameLower}_item.xml`
      ]
    ];

    templateFiles.forEach(([src, dest = src]) => {
      this.fs.copyTpl(`${this.sourceRoot()}/${src}`, `${dest}`, this.props);
    });

    /* Add service method in Core */

    const coreImports =
      `import ${this.props.packageName}.core.data.${this.props.entityNameLower}.${this.props.entityName}Dto\n` +
      `import ${this.props.packageName}.core.data.${this.props.entityNameLower}.${this.props.entityName}RestResource\n` +
      `import ${this.props.packageName}.core.data.${this.props.entityNameLower}.${this.props.entityName}Service\n` +
      '//import-needle\n';

    const coreService =
      `    fun ${this.props.entityNameLower}Service(): ${
        this.props.entityName
      }Service {\n` +
      `        val resource = ${
        this.props.entityName
      }RestResource(configuration.serverUrl,GgaRest.ws(),jhiUsers)\n` +
      `        return ${
        this.props.entityName
      }Service(resource, CombinedCache(preferences,serializer))\n` +
      `    }\n` +
      '//services-needle\n';

    this.fs.copy(
      `app/src/main/java/${packageDir}/core/Core.kt`,
      `app/src/main/java/${packageDir}/core/Core.kt`,
      {
        process: function(content) {
          let regEx = new RegExp('//import-needle', 'g');
          let newContent = content.toString().replace(regEx, coreImports);
          regEx = new RegExp('//services-needle', 'g');
          newContent = newContent.toString().replace(regEx, coreService);
          return newContent;
        }
      }
    );

    /* Add menu option call in activity */

    const menuOptionCallback =
      `             R.id.nav_${this.props.entityNameLower} -> {\n` +
      `               startActivity(${
        this.props.entityName
      }Activity.openIntent(this))\n` +
      '             }' +
      '//options-needle\n';

    this.fs.copy(
      `app/src/main/java/${packageDir}/MainActivity.kt`,
      `app/src/main/java/${packageDir}/MainActivity.kt`,
      {
        process: function(content) {
          let regEx = new RegExp('//options-needle', 'g');
          return content.toString().replace(regEx, menuOptionCallback);
        }
      }
    );

    /* Add menu item in navigation drawer */

    const menuItem =
      `<item\n` +
      `android:id="@+id/nav_${this.props.entityNameLower}"\n` +
      `android:icon="@drawable/ic_menu_camera"\n` +
      `android:title="@string/${this.props.entityNameLower}"\n` +
      '/>' +
      '<!--<menu-needle-->\n';

    this.fs.copy(
      `app/src/main/res/menu/activity_main_drawer.xml`,
      `app/src/main/res/menu/activity_main_drawer.xml`,
      {
        process: function(content) {
          let regEx = new RegExp('<!--<menu-needle-->', 'g');
          return content.toString().replace(regEx, menuItem);
        }
      }
    );

    /* Add string resources */

    const stringValue =
      `    <string name="${this.props.entityNameLower}">${this.props.entityName}</string>\n` +
      `    <string name="title_activity_${this.props.entityNameLower}">${
        this.props.entityName
      }</string>\n` +
      '    <!--strings-needle-->\n';

    this.fs.copy(
      `app/src/main/res/values/strings.xml`,
      `app/src/main/res/values/strings.xml`,
      {
        process: function(content) {
          let regEx = new RegExp('<!--strings-needle-->', 'g');
          return content.toString().replace(regEx, stringValue);
        }
      }
    );

    /* Add activity in manifest */

    const manifestActivity =
      '    <activity\n' +
      `            android:name=".${this.props.entityName}Activity"\n` +
      `            android:label="@string/title_activity_${
        this.props.entityNameLower
      }"\n` +
      '            android:theme="@style/AppTheme.NoActionBar">\n' +
      '            <meta-data\n' +
      '                android:name="android.support.PARENT_ACTIVITY"\n' +
      `                android:value="${this.props.packageName}.BaseActivity" />\n` +
      '        </activity>\n' +
      '      <!--activity-needle-->\n';

    this.fs.copy(`app/src/main/AndroidManifest.xml`, `app/src/main/AndroidManifest.xml`, {
      process: function(content) {
        let regEx = new RegExp('<!--activity-needle-->', 'g');
        return content.toString().replace(regEx, manifestActivity);
      }
    });
  }

  install() {
    // This.installDependencies();
  }
};
