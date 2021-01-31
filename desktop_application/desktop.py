import pyrebase
from getpass import getpass
import pyperclip
import os
import time
from decouple import config
try:
    uname_ = os.system(f"uname")
except:
    pass

cred = {
    "apiKey": config("apiKey"),
    "type": config("type"),
    "databaseURL": config("databaseURL"),
    "authDomain": config("authDomain"),
    "project_number": config("project_number"),
    "firebase_url": config("firebase_url"),
    "project_id": config("project_id"),
    "storageBucket": config("storageBucket"),
}

# initialisatiing pyrebase
firebase = pyrebase.initialize_app(cred)
auth = firebase.auth()
# initialisatiing Database
db = firebase.database()

# Reset Password\n4. Email Verification

while True:
    choice = int(input("1. LogIn:\n2. Create Account\n3. Exit\n"))
    if choice == 1:
        email = input("Please Enter Your Email Address : \n")
        password = getpass("Please Enter Your Password : \n")
        # email = str(config("firemail"))
        # password = str(config("firepass"))
        print(email, password)
        try:
            # email = input("Enter email\n")
            # passowrd = input("Enter password\n")
            login = auth.sign_in_with_email_and_password(email, password)
            
            # print(login["localId"])
            # uid = login["localId"]
            # print(token)
            # uid = token
            # print(auth.current_user['localId'])
        except:
            print("WRONG PASSWORD TRY AGAIN") 
            continue
    
        uid=auth.current_user['localId']
        if len(str(uid))!=0:
            print("CONNECTED And Running........")
        recent_value = ""
        while True:    
            print("APP RUNNING IN BACKGROUND, CHECKING EVERY 5 SECONDS")    
            tmp_value = pyperclip.paste()
            if tmp_value != recent_value:
                recent_value = tmp_value
                db.child("users").child(uid).child("winClipboard").set(recent_value)
                db.child("users").child(uid).child("android").set("0")
            if db.child("users").child(uid).child("android").get().val() != "0":
                if uname_ == "Linux":
                    os.system(
                    f"echo {db.child('users').child(uid).child('androidClipboard').get().val()} | xclip -selection clipboard"
                    )
                else:
                    pyperclip.copy(db.child("users").child(uid).child("androidClipboard").get().val())
            time.sleep(5)

    elif choice == 2:
        # email = input("Please Enter Your Email Address : \n")
        # password = getpass("Please Enter Your Password : \n")
        # create users
        try:
            auth.create_user_with_email_and_password(email, password)
            print("Account created! Success .... ")
        except:
            print("Error")

    # elif choice == 4:
    #     # send email verification
    #     try:
    #         auth.send_email_verification(login["idToken"])
    #         print("SENT")
    #     except:
    #         print("ERROR")

    # elif choice == 3:
    #     # reset the password
    #     try:
    #         auth.send_password_reset_email(email)
    #         print("sent")
    #     except:
    #         print("Error")

    elif choice == 3:
        break
    else:
        print("Choose your option Correctly")
        continue
