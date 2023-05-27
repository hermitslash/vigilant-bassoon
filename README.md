# reporting_svc

Reporting Services for small business.

## Pre Requisites
1) java17
2) maven
3) postgres/yugabyte (Using yugabyte) Please change properties(application.properties) as required.

### Generate Keys
# create key pair
openssl genrsa -out generalkp.pem 2048

# extract public key
openssl rsa -in generalkp.pem -pubout -out generalpuk.pem

# extract private key
openssl pkcs8 -in generalkp.pem -topk8 -nocrypt -inform PEM -outform PEM -out generalprk.pem

# generate keystore
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650



## TBD
1) Batch Report Generation with csv Files (Created from excel).
2) Integration Tests.
3) Support multiple company accounts.

__Any support will be appriciated__
Please raise a PR to add a feature or fix a bug.

Contact me
1) @chinmai_d ---> twitter
2) chinmai.dbharadwaj@gmail.com
3) Slack - [#tech-sme-discussions](https://shreshtha-workspace.slack.com/archives/C052FC5PH2P)