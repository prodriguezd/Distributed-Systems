#include "chat.h"

extern int running;
extern CLIENT *clnt;
extern int lineas;

extern WINDOW  *chatWin;
extern int writing;

void getMessages();
void handle_alarm( int sig );

void *threadChat(void *arg) {

    signal(SIGALRM, handle_alarm);
    alarm(1);

    while (running == 1) {
        
    }

    pthread_exit(NULL);
	return NULL;
}

void getMessages() {
    
    char * *result_1;
    int  *result_3;
	char *getlineas_1_arg;

    result_3 = getlineas_1((void*)&getlineas_1_arg, clnt);
	if (result_3 == (int *) NULL) {
		clnt_perror (clnt, "call failed");
	}

    if (lineas < *result_3) {

        while (lineas < *result_3) {
            result_1 = getchat_1(&lineas, clnt);
            if (result_1 == (char **) NULL) {
                clnt_perror (clnt, "call failed");
            }
            while (1) {
                if (writing == 0) break;
            }

            writing = 1;

            wprintw(chatWin, "%s", *result_1);
            wrefresh(chatWin);

            lineas++;

            free(*result_1);
            
            writing = 0;
        }
    }
}

void handle_alarm( int sig ) {

    getMessages();

    alarm(1);
    signal(SIGALRM, handle_alarm);
}


