#!/bin/bash

for i in auth-service email-service eureka-server gateway-server order-service product-mplace-service profile-service static-server ui
do
	cd $i
	docker build -t $i -f Devops/Dockerfile .
	cd ..
done