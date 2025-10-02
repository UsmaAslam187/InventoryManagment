### Dev Image

docker build -f Dockerfile.dev -t stockspree-app-dev .

### Stage Image
docker image build -f Dockerfile -t rehantf/tf:stockspree-staging-java-img .

### Prod Image
docker image build -f Dockerfile -t rehantf/tf:stockspree-prod-java-img .


mvn -Dtest=C_CreateProductInboundPortTest.java test