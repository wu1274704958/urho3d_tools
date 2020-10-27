#include <SDL/SDL.h>
#include <cstring>
#ifdef WIN32
#include <Windows.h>
#endif
#include <cstdio>
SDL_Window* window;
int InitVideo()
{
    if (0 != SDL_Init(SDL_INIT_EVERYTHING))
		return -1;
	window = SDL_CreateWindow("first", 30, 30, 300, 400, 0);
	if (window == NULL)
		return -2;
}
void Redraw()
{

}

Sint32 cursor = 0;
Sint32 selection_len = 0;
#ifdef WIN32
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE prevInstance, PSTR cmdLine, int showCmd) 
{ 
#else
int main(int argc, char** argv) 
{ 
#endif
    SDL_bool done = SDL_FALSE;

    InitVideo();
    /* ... */
    
    char text[1024] = {0};
    char composition[1024] = {0};
#ifdef WIN32
    AllocConsole();
    freopen("conin$","r+t",stdin);
    freopen("conout$","w+t",stdout);
#endif
    
    while (!done) {
        SDL_Event event;
        if (SDL_PollEvent(&event)) {
            switch (event.type) {
                case SDL_MOUSEBUTTONDOWN:
                {
                    SDL_StartTextInput();
                    //int ret = SDL_SetWindowInputFocus(window);
                    SDL_Rect rect;
                    rect.x = event.motion.x;
                    rect.y = event.motion.y;
                    rect.w = 50;
                    rect.h = 50;
                    SDL_SetTextInputRect(&rect);
                    printf("mouse button down! %d %d\n",event.motion.x,event.motion.y);
                }
                break;
                case SDL_KEYUP:
                {
                    if(event.key.keysym.sym == SDLK_o)
                    {
                        SDL_StopTextInput();
                        printf("mouse button up \n");
                    }
                }
                break;
                case SDL_QUIT:
                    /* Quit */
                    done = SDL_TRUE;
                    break;
                case SDL_TEXTINPUT:
                    /* Add new text onto the end of our text */
                    strcat(text, event.text.text);
                    break;
                case SDL_TEXTEDITING:
                    /*
                    Update the composition text.
                    Update the cursor position.
                    Update the selection length (if any).
                    */
                    strcpy(composition,event.edit.text);
                    cursor = event.edit.start;
                    selection_len = event.edit.length;
                    break;
            }
        }
        Redraw();
    }

    SDL_Quit();
#ifdef WIN32
    FreeConsole();
#endif
    return 0;
}
