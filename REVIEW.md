#Review Garritsen-pset6
## commit: Delete LoadActivity.java
### by Tristan Hoobroeckx

####Names: 2  
**MainActivity line 79-80:** emailET and passET, not descriptive enough and no standard casing (PascalCasing, camelCasing).  
**MainActivity line 117-118:** emailET and passET are declared and initiated a second time. Make global and use again.  
**MainActivity line 126-127:** emailET and passET are declared and initiated a third time. Make global and use again.  
**SignupActivity:** Sign Up are two separate words for some reason and should be treated as such when casing. Changes Activity name to SignUpActivity.  
**SignupActivity line 30:** String ACCOUNT_TAG is not a constant or a TABLE. Write in lowercase with casing i.e. accountTag. If it is a constant and I am mistaken, disregard my commenting.  
**SignupActivity line 107:** accountInfo is a somewhat undescriptive name for a method that saves account information and then sends you back one screen. Advice to change to saveInfo.  
**SocialActivity line 38:** I'm starting to think these are constants. A comment would be nice here.  
**ProfileActivity line 55:** boredET does not adhere to camelCasing. boredEditText would be a better name.

####Headers: 4  
**MainActivity:** *Header is fine. Not too long not too short.*  
**SignupActivity:** *Header is fine. Not too long not too short.*  
**SocialActivity:** *Header is fine. Not too long not too short.*    
**ProfileActivity:** *Header is fine. Not too long not too short.*

####Comments: 3  
**MainActivity line 30:** I would have liked to have a comment here that explains this constant.  
**SignupActivity:** *Comments are present and concise.*  
**SocialActivity line 38:** I would have liked a comment here that explains this constant.  
**ProfileActivity line 34:** I would have liked a comment here that explains this constant.  

  
####Layout: 3  
**MainActivity:** *Good layout, very readable.*  
**SignupActivity:** *Good layout, very readable.*  
**SocialActivity:** In MainActivity and SignupActivity your order was 'Lifecycle components', 'Create', 'The rest'. Here your methods that create are pushed to the bottom. While this might be a logical order, it is not consistent with the other Activities.  
**ProfileActivity:** Again pick an order!


####Formatting: 4  
**MainActivity:** *Formatting is easily readable.*  
**SignupActivity:** *Formatting is easily readable.*  
**SocialActivity:** *Formatting is easily readable.*  
**ProfileActivity:** *Formatting is easily readable.*

####Flow: 3  
**MainActivity line 116-132:** methods fastLogIn2 and fastLogIn3 are duplicates. Write code once in third method and call from other two.  
**SignupActivity:** *Flow seems to be good.*  
**SocialActivity line 36:** authTest is never used. Remove or use.  
**ProfileActivity line 32:** authTest is never used. Remove or use.

####Idiom: 4  
**MainActivity:** *no comments.*  
**SignupActivity:** *no comments.*  
**SocialActivity:** *no comments.*

####Expressions: 3  
**MainActivity line 119-120:** these expressions are hard-coded. Consider placing values in resource file and retrieving from there.  
**MainActivity line 128-129:** these expressions are hard-coded. Consider placing values in resource file and retrieving from there.  
**SignupActivity:** *All expressions seem to be alright.*  
**SocialAcitivity line 41 and 60-62:** Why fill allBoredPeople with string and then immediately in onCreate() replace that string? Comment would be nice.  
**ProfileActivity line 129 and 143 and 152:** Hard-coded!

####Decomposition: 4  
**MainActivitity:** *no comments.*  
**SignupActivity:** *no comments.*  
**SocialActivity:** *no comments.*  
**ProfileActivity:** *no comments.*

####Modularization: 4
**Remember that when something has to be done multiple times, its better to put it in a class and then call its methods.** Just some advice.

####Last Comments/Error Prevention:  
**SignupActivity line 81:** You check whether a password is longer than zero digits, when in fact Firebase requires passwords of 6>. This means that right now in your application, users can create passwords that will not work. Firebase will not report on this and the user will not be able to login. 

Always give View an id. That way the are easily discernible and you will be able to position them relative to each other. It is just good practice in case you need an id.

You have a lot of hard-coded strings and values in your java and xml files. Utilize the resource files to manage your resources!

The background colour for your profile layout is black. This seems just like a bad design decision to me. Either give all screens black backgrounds or don't give any of them black backgrounds. 

You forgot to remove the layout file for LoadActivity.java.

