Step 1 : Build App demoing usage of Apache curator leaderlatch usage
         The App should connect to local zookeeper to acquire latch with path = 'instance_primary'
         The App should keep printing to console 'Primary' or 'Secondary' depending on if it could acquire latch or not

Step 2 : Create Dockerfile to create image

Step 3 : Deploy 2 instances on the minikube local cluster and confirm from logs only one of them prints 'Primary'

Step 4 : Bring down one instance and confirm the behavior of second instance

Step 5 : Bring down zookeeper and see what happens

TODO : 
https://cloud.google.com/blog/products/gcp/kubernetes-best-practices-mapping-external-services

from minikube, possible to connect to zk cluster running locally?
if not, create deployment for zk cluster in minikube cluster, expose as a service and then connect your app to zk
Or, deploy sidecar container to connect to zk locally


https://stackoverflow.com/questions/55667607/where-do-i-find-the-host-ip-address-for-an-app-deployed-in-minikube
ifconfig - docker0
172.17.0.1



What worked 
https://medium.com/tarkalabs/proxying-services-into-minikube-8355db0065fd
Accessing service on local machine from minikube VM

> on laptop where zk server is running at 2181
ssh -i $(minikube ssh-key) -R 3181:localhost:2181 docker@$(minikube ip)

then, accessing localhost:3181 inside the cluster 
netstat -an | grep 3181