# Build:
#   docker build --rm -t catalog-service-tracing:1.0.17 .
#   docker tag catalog-service-tracing:1.0.17 docker.io/rhtgptetraining/catalog-service-tracing:1.0.17

# Docker push (on F28):
#   Make sure the following is the *first* entry in the docker daemon (by updating /etc/sysconfig/docker):  --add-registry docker.io
#   docker login docker.io -u rhtgptetraining
#   docker push docker.io/rhtgptetraining/catalog-service-tracing:1.0.17

# Execution: OpenShift
#   oc run catalog-service-tracing --image=docker.io/rhtgptetraining/catalog-service-tracing:1.0.17

FROM redhat-openjdk-18/openjdk18-openshift:1.5-14
ENV JAVA_APP_DIR=/deployments
ENV AB_OFF=true
EXPOSE 8080 8778 9779
COPY target/catalog-service-tracing-1.0.17.jar /deployments/
