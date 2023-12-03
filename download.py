import sys
import pafy

def cb(total, recvd, ratio, rate, eta):
    state = round(ratio*100)
    print(state)


def download(url, song):
    audio = pafy.new(url)
    best_audio = audio.getbestaudio(preftype="m4a")
    file = "Music\\"+song+".m4a"
    best_audio.download(filepath = file, quiet = True, callback=cb)

if __name__ == '__main__':
    url = sys.argv[1]
    song = sys.argv[2]
    download(url, song)
