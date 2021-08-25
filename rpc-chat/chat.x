program PROGRAMA_CHAT {
	version VERSION_CHAT {
		string getChat(int startLine) = 1;
		void writeChat(string) = 2;
		int getLineas() = 3;
	} = 1;
} = 0x20000001;
