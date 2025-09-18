### Dev Image

docker build -f Dockerfile.dev -t stockspree-app-dev .

### Stage Image
docker image build -f Dockerfile -t rehantf/tf:stockspree-staging-java-img .