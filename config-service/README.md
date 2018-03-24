# Config-Service
Configuration server with basic authentication and symetric encrypter.

### Example
Get the properties for **urban-service** with **dev** profile
```sh
$ curl http://root:s3cr3t@localhost:8888/urban-service-dev.yml
```
Response:
```
myname: Hernan
password:
  encrypted: c00lPassw0rd
server:
  port: 0
  ```
  
  
  ### Encrypt a password
  To encrype a password use as follows:
  ```sh
$ curl http://root:s3cr3t@localhost:8888/encrypt -d Herny
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100    69  100    64  100     5     64      5  0:00:01 --:--:--  0:00:01 69000 a9cd127e22ed17ad7f72892763b7131d170ce0a08dfbf631dca88cd1f9cc09fd
  ```
Now you can store it in the config file with the {cipher} prefix as follows:
`password={cipher}a9cd127e22ed17ad7f72892763b7131d170ce0a08dfbf631dca88cd1f9cc09fd`

You can test if it works using the decrypt endpoint as follows:
```bash
$ curl http://root:s3cr3t@localhost:8888/decrypt -d a9cd127e22ed17ad7f72892763b7131d170ce0a08dfbf631dca88cd1f9cc09fd
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100    69  100     5  100    64      5     64  0:00:01 --:--:--  0:00:01  4312 Herny

```