# Android HackPack Tutorial 2022
Updated by Sathvik Nallamalli

### Overview
For the Android Hackpack, we are going to develop **Personal To-Do List** - A app to keep track of your to-do list items. In this tutorial, you'll learn basic process behind making android apps such as developing an UI, storing data on a real database - Firebase, opening activity from another activity and much more. 
So Let's get started!

### Clone this Project
* If you don't have [Android Project](https://developer.android.com/studio/) installed on your computer, install it from [here](https://developer.android.com/studio/).

* In the **Welcome to Android Studio** window, click **Start a new Android Studio project**.
  ![Android Studio](/docs_assets/as.png)

* Start by **Cloning this starter project** , to get access to the starter files that you will use to build the app. Run the following command through terminal at your preferred directory location
  * **Command** : "git clone https://github.com/TreeHacks/hackpack-android-firebase.git"
  Exit terminal and open the downloaded project in Android Studio
   
After some processing, Android Studio opens the IDE.

![](/docs_assets/fs.png)

Now let's take a moment to review the most important files.

First, be sure the **Project window is open (select View > Tool Windows > Project)** and the Android view is selected from the drop-down list at the top of that window. You can then see the following files:

**app > src > main > java > Splash Activity**
This is the splash screen which will be the launch screen that your app displays to welcome the user. When you build and run the app, the system launches an instance of this Activity and loads its layout.

**app > res > layout > activity_main.xml**
This XML file defines the layout for the activity's (the next activity displayed after the launch screen) UI. It contains a TextView element with the text "Hello world!".

**app > manifests > AndroidManifest.xml**
The manifest file describes the fundamental characteristics of the app and defines each of its components.

**Gradle Scripts > build.gradle**
You'll see two files with this name: one for the project and one for the "app" module. Each module has its own build.gradle file, but this project currently has just one module. You'll mostly work with the module's build.gradle file to configure how the Gradle tools compile and build your app. 

### Run the app
Press the run button (circled in following figure) and you will see a hello world written in middle of screen. You will need to add a emulator/device to run the app. Just follow the default steps to add emulator or plug your android device using usb.
![](/docs_assets/run.png)

## Now Let's Start Building our Todo-List App

Let's define our app design. It will be two activity (excluding the launch screen) application - 

- **SplashActivity**: This will be the launch screen of the app - 

  ![](/docs_assets/splash.png) 

- **MainActivity**: This will show the list of all the to-do items already in the database (will be empty when first developing) and also will include a button to add a new to-do list item. Finally, it would look like this - 

  ![](/docs_assets/main_activity.png) 

- **AddActivity**: This will be the add activity where you will be able to add a new todo list item, and it will saved to your database - 

  ![](/docs_assets/add_activity.png)

### Lets build our User Interface (UI)

UI in android is build by using Design editor (Preferred way) or by using text editor. All the UI in android is declared using XML and can be found in **app > res > layout**.

Edit **app > res > layout > activity_main.xml ** as follows and let's see what we are doing

```html
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
        android:layout_height="wrap_content"
        android:id="@+id/pullToRefresh"
        android:layout_width="match_parent" >

        <ListView
            android:id="@+id/toDoList"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="10dp"></ListView>
    </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>

</LinearLayout>
```

In this layout, we have added a **\<ListView> tag** which is a view that can show list of elements in scrollable fashion. It is wrapped in a SwipeRefreshLayout that allows the traditional pull up movement to refresh the data. We will use ListView to show list of already to-do items and we have given it **id as toDoList** so as to get its reference later. Our list view will be custom to include our own layout for the item row (to include a button and checkbox)

To do that, we will be using some helper files: ListItem and ListItemAdapter to make a custom adapter for our list. 

Similary, for add activity, create a new activity by **File > New > Activity > Empty Activity** , name the activity to AddActivity and selecting all the other default values. This will generate two files:-

- **AddActivity.java** in app > java > io.your_name.hackerpad 
- **activity_add** in app > res > layout

Edit activity_add with this repository's activity_add file. Its a simple layout file with one field to take user input for a new todolist item & a button to add the note to the database.

### Let's Build our Model - List Item

Model refers to the Plain objects needed for the application which in our case is clearly a List Item.
So, we will define a ListItem class with following fields:

- text - A string field which will contain the actual item text
- - key - A string feild which will be the key of the to-do list item in our database
- isCheck - A boolean feild which will show if a note is checked or not 

### Now lets define our Custom Adapter - ListItemAdapter

The ListAdapter file will contain the code to set each list item to be based on the data from the database and properly add functions to each box and button click. 

```java
Context context;
    ArrayList<ListItem> itemsList = null;

    public ListItemAdapter(Context context, int resource, ArrayList<ListItem> arraylistcheckedbooks) {
        super(context, resource, arraylistcheckedbooks);
        this.context = context;
        this.itemsList = arraylistcheckedbooks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem listItem = itemsList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        }


        CheckBox cb = convertView.findViewById(R.id.itemText);
        Button edit = convertView.findViewById(R.id.editButton);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dr = mDatabase.child("myList").child(listItem.getKey());
                dr.child("isCheck").setValue(cb.isChecked());
            }
        });

        cb.setText(listItem.getText());
        cb.setChecked(listItem.isChecked);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit to-do");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference dr = mDatabase.child("myList").child(listItem.getKey());
                        dr.child("text").setValue(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });


        return convertView;
    }
```

As clear by the function signatures, this interface defines all the necessary operations we want to do with our data like Editing a new note, Checking/Unchecking it. We will retrieve all our data in MainActivity.java

Lets implement these in the **MainActivity** class.

```java
    ArrayList<ListItem> items;

    ListAdapter adapter;

    ListView todolist;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gatherdata(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        todolist = (ListView) findViewById(R.id.toDoList);

        gatherdata();


    }

    private void gatherdata() {
        items = new ArrayList<ListItem>();

        DatabaseReference eventref = FirebaseDatabase.getInstance().getReference().child("myList");

        eventref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    items.add(new ListItem(snapshot.child("text").getValue().toString(),
                            Boolean.parseBoolean(snapshot.child("isCheck").getValue().toString()),
                            snapshot.getKey()));
                }

                //set adapter
                adapter = new ListItemAdapter(MainActivity.this, R.layout.list_row, items);
                todolist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addButton) {
            Intent intent = new Intent(getApplicationContext(), AddItem.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
```

### Cloud Storage

For storing our data we will use Google's **Firebase Database** which is a realtime database that integrates with iOS and Android mobile apps using a hierarchial database structure with children and parent nodes. It uses a Key-Value pairing. The integration with Android makes Firebase so easy and streamlined as opposed to using options such as Sqlite or Mongo. 

To integrate, we need to create a project in Firebase, enter our app information, and download an IMPORTANT google-services.json file to upload in our Android project. This file is what allows communication between our app and Firebase. The following dependencies have already been added to your starter code for integration with Firebase. 

project/build.graddle: 

```java
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.0.2"
        **classpath 'com.google.gms:google-services:4.3.10'**
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

app/build.gradle
```java
apply plugin: 'com.android.application'
**apply plugin: 'com.google.gms.google-services'**

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"
    compileOptions {
        sourceCompatibility 1.8
        targetCompatibility 1.8
    }

    defaultConfig {
        applicationId "com.example.personaltodolist_starter"
        minSdkVersion 19
        targetSdkVersion 30
        versionCode 1
        versionName "1.0"
        multiDexEnabled true


        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.2'
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'

    **implementation platform('com.google.firebase:firebase-bom:29.0.4')
    implementation 'com.google.firebase:firebase-database'**

}
```

Our final database will look this when populated with some items:
![](/docs_assets/database.png)

### Writing to the Database

Now its time to allow for users to add a new to-do list item to the database and therefore update in our MainActivity when we display the full list. 

```java
 	//AddActivity.java
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        EditText newItemText = findViewById(R.id.newItemText);
        Button add = findViewById(R.id.addItem);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dr = mDatabase.child("myList").push();
                dr.child("text").setValue(newItemText.getText().toString());
                dr.child("isCheck").setValue(false);

                Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
```

## Hit Run

Now hit run and you can see a working prototype of your Personal To-Do List application

## What Next

There are lot of things that you can do on top of this application.

1. [Google Speech API](https://cloud.google.com/speech-to-text/docs/) : Use Google Speech API to take audio notes or translate a given speech into English Text.
2. [Google Voice API](https://developers.google.com/voice-actions/system/) : Use Google Voice API to add custom functionalities on "Ok Google" Command.
3. [Google APIs for Android](https://developers.google.com/android/) : A one-stop collection for all the google apis documentation and codelabs for Android.
4. Add more UI elements to the app to let users pin, delete, and customize their notes.

### More Resources

Android development can be overwhelming in the beginning but there are lots of learning resources available on internet. Feel free to go through them:

- [Official Android Documenation](https://developer.android.com/guide/): - One of the best
- [Android Fundamentals Training by Google Codelab](https://developer.android.com/courses/fundamentals-training/toc-v2) - A full list of code lab style tutorial by Google for teaching android from fundamentals to advanced. (Highly recommended!)
- [Sunshine Weather App - Google CodeLab](https://codelabs.developers.google.com/codelabs/build-app-with-arch-components/index.html?index=..%2F..index#0) - A good end to end app following recommended architecture pattern.

### License
MIT

# About HackPacks 🌲

HackPacks are built by the [TreeHacks](https://www.treehacks.com/) team and contributors to help hackers build great projects at our hackathon that happens every February at Stanford. We believe that everyone of every skill level can learn to make awesome things, and this is one way we help facilitate hacker culture. We open source our hackpacks (along with our internal tech) so everyone can learn from and use them! Feel free to use these at your own hackathons, workshops, and anything else that promotes building :) 

If you're interested in attending TreeHacks, you can apply on our [website](https://www.treehacks.com/) during the application period.

You can follow us here on [GitHub](https://github.com/treehacks) to see all the open source work we do (we love issues, contributions, and feedback of any kind!), and on [Facebook](https://facebook.com/treehacks), [Twitter](https://twitter.com/hackwithtrees), and [Instagram](https://instagram.com/hackwithtrees) to see general updates from TreeHacks. 

