# mail-sink

MailSink runs a super simple SMTP server which traps any message sent to it to display in a web interface.

### How to use?

Docker image is available from docker hub. 
`docker run -p 1025:3025 -p 8080:8080 koneru9999/mail-sink:latest`

If just want to test with dummy data, run docker with DUMMY_DATA_SIZE environment variable
`docker run -p 1025:3025 -p 8080:8080 -e DUMMY_DATA_SIZE=20 koneru9999/mail-sink:latest`