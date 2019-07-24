# groupomaniaclient

## How to run the code on your system:  
  
- Clone the repository with the following command: `git clone https://github.com/nanoandrew4/groupomaniaclient.git`
- Change directories into the cloned repository
- Change directories into the 'scripts/' directory
- Run the build script for the environment you want to start (either dev or prod)

#### Running the development instance of the client
The development instance simply starts up the server on your host machine, no containers
are created. This allows shorter start times, and remote debugging, which are two important
elements to consider when developing locally.

#### Running a production ready instance of the client
The client requires that the backend container be healthy, otherwise the docker-compose setup
will fail to start. Visit [https://github.com/nanoandrew4/groupomania-web] for information on
cloning and setting up the backend.

To stop the production setup, change directories to the root of the project, and then `docker/prod`.
Once here, run `docker-compose stop` in order to stop the containers, so they can later be restarted
with `docker-compose start`. To stop and delete the containers, run `docker-compose down`.