#include "chat.h"

static int blocked = 0;
static int numLineas;

char **getchat_1_svc(int *argp, struct svc_req *rqstp) {
	static char * result;
	FILE *file;

	char *messages  = (char*)malloc(sizeof(char));
	int index = 0;

	while (1) {
		if (blocked == 0) break;
	}

	blocked = 1;

	file = fopen("messages.txt", "r");
	if (file != NULL) {

		int counterLineas = 0;
		
		char ch;
		

		if (*argp != 0) {

			
			for (ch = getc(file); ch != EOF; ch = getc(file)) {
        		if (ch == '\n') // Increment count if this character is newline 
            	counterLineas++; 
			

				if (counterLineas == *argp) break;
			}
		}

		while ((ch = fgetc(file)) != EOF) {
			messages[index] = ch;
			messages = (char*)realloc(messages, sizeof(char) * (index + 2));
			index++;
			if (ch == '\n') break;
		}
			
	}


	messages[index] = '\0';
	fclose(file);

	blocked = 0;
	result = messages;
	return &result;
}

void *writechat_1_svc(char **argp, struct svc_req *rqstp) {
	static char * result;
	FILE *file;

	while (1) {
		if (blocked == 0) break;
	}

	blocked = 1;

	file = fopen("messages.txt", "a+");

	if (file != NULL) {
		fprintf(file, "%s", *argp);
	}
	else {
		printf("Unable to write in file. Message lost-> %s", *argp);
	}
	fclose(file);

	blocked = 0;

	return (void *) &result;
}

int *getlineas_1_svc(void *argp, struct svc_req *rqstp) {
	//return &numLineas;
	numLineas = 0;

	FILE *file;
	char ch;

	while (1) {
		if (blocked == 0) break;
	}

	blocked = 1;

	file = fopen("messages.txt", "r+");

	if (file != NULL) {
		for (ch = getc(file); ch != EOF; ch = getc(file)) 
        	if (ch == '\n') // Increment count if this character is newline 
            	numLineas++; 
	}

	blocked = 0;

	printf("\ntengo %d lineas", numLineas);
	
	fclose(file);

	return &numLineas;		


}
