## Run latest release

1. Make sure at least OPENJDK 22.0.1 is installed and setup
2. Setup email.conf file with your email credentials
3. run the jar file with `java -jar 2FA-Server.jar`

## Setup email.conf

Email was tested with smtp.gmail.com. Other endpoints might not work or need modifications.

email.conf is a simple text file with six lines. Replace the placeholders with your email credentials.

File structure:

```
username
AppPassword
smtp.gmail.com
587
true
true
```