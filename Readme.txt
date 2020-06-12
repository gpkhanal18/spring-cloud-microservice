for the limit service application , to pick configuration from config server it has to be 
committed and refresh the configuration. this is a problem when we have so many instances 
so we can use spring cloud bus to refresh once and actually refresh all the instances to 
pick configuraitons from updated git. 


1. currency conversion service talks to currency exchange service through naming server using 
ribbon which also balances the load. 

2. we added feign for easy access to services rather than resttemplate. 

3. we added ribbon and load balances the call to exchange service by hardcoding the url. 
this was not the good approach so we added the naming server so that ribbon can get 
the info of instances through naming server. 

4. we told ribbon talk to naming service and get the url of exchange service from conversion 
service. 

5. registered each of these services with naming server using netflix eureka client and 
enabling it using @EnableDiscoveryClient. 

naming server - registers services 
ribbon - client side load balancing 
feign - easier rest client, proxy based. 
api gateway - intercept request. 
spring cloud slouth zipkin - 

6. now we are adding zull for api gateway between microservices. it will intercept the calls
to the microservices. basically zull has to pass the api call to then instead of calling to 
exchange service we call to api gateway like this 
http://localhost:8765/currency-exchange-service/currency-exchange/from/AUS/to/INR instead of 
http://localhost:8001/currency-exchange/from/AUS/to/INR

7. we can also route any direct traffic to exchange service through api gateway. by changing 
the configuration for feign proxy in currency calculation service. zuul uses app name to talk
to the naming server. 

8. Incase we have any issues among these services i would want one place to go and look 
for any issues. by using spring cloud slouth - using zipkin - its a distributed tracing system. 
all the request will be provided with the unique id. 

9. now we want to centralize the logs from all the microservices which makes us easier to
search for the logs. for this we use. sonns are ELK , ZipKinDistributed Tracing Server and 
use the ui provided by zipkin to look for the specific request. For this we will use RabbitMQ
to send messages from microservices to zipking. services puts messages in queue and zipkin 
will pull it from queue. 

10. zipkin(will have a db) consumes messages from rabbit mq . microservices sends
 messages to rabbit mq. 

11. how to put messages into rabbit mq? 
	a. just adding the dependency and configuring microservices
	b. then we can view logs using zipkin ui in browser. 
	c. it brings visibility to the microservice call. 
	
	
12. Regarding fault tolerence of microservices.
hystrix framework helpus us to build fault tolerence microservices. add a hystrix 
dependency and then enable it. @EnableHystrix.  when you add @HystrixCommand and provide 
fallbackMethod as an atribute you can provide an alternate response incase that service 
is throwing exception.  




	






