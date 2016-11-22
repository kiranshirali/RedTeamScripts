# Modified script. Original source https://stackoverflow.com/questions/26358994/how-to-get-properties-of-authentification-alias-on-was-7-using-wsadmin

import sys, java, java.io, java.lang, base64, binascii

resFile="Results.res"

def search ( alias, file ):
    f=open(file)
    lines=f.readlines()
    for line in lines:
        poz = line.find('alias=\"'+alias)
        if poz > 0:
            Line = line
            break

    user = Line[Line.find('userId=')+8:Line.find('\" password')]
    password = Line[Line.find('password=')+15:Line.find('\" description')]

    password = decrypt(password)
    description = Line[Line.find('description=')+13:Line.find('\"/>')]

    write ( alias, user, password, description, resFile)

def write ( alias, user, password, desc, file ):
    objItemFileOutputStream = java.io.FileOutputStream(file, 1)     #apend la sfirsit fisier
    objItemFileOutputStream.write('\n')
    AuthList = "AuthDataAlias = [\\\n[\'"+alias+"\', \'"+user+"\', \'"+password+"\', \'"+desc+"\'] ]" 
    objItemFileOutputStream.write(AuthList)


def decrypt ( word ):
    if not len(word) > 1: exit()
    word = word.replace(':', '')
    value1 = binascii.a2b_base64(word)
    value2 = '_' * len(value1)
    out = ''
    for a, b in zip(value1, value2):
        out = ''.join([out, chr(ord(a) ^ ord(b))])
    return out


#MAIN
search ( "ALIAS_MENTIONED_IN_THE_SECURITY_FILE", "PATH_TO_SECURITY_FILE (C:\WebSphere\profiles\Node1\config\cells\DmgrProdCell\security.xml)")