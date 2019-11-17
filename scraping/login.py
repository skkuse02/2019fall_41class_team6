from selenium import webdriver
from bs4 import BeautifulSoup as bs
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains
import time
import os
import re
from urllib.request import urlopen
import json
from pandas.io.json import json_normalize
import pandas as pd, numpy as np
import requests
username = input("Enter your username")
password = input("Enter your password")
br = webdriver.Chrome('C:/Users/Zapata/Desktop/Test/chromedriver.exe')
br.get('https://www.instagram.com/accounts/login/')
time.sleep(3)
br.find_element_by_name('username').send_keys(username)
br.find_element_by_name('password').send_keys(password, Keys.ENTER)
time.sleep(3)

br.get('https://www.instagram.com/'+username)
time.sleep(5)
Pagelength = br.execute_script("window.scrollTo(0, document.body.scrollHeight);")

links = []
source = br.page_source
data = bs(source, 'html.parser')
body = data.find('body')
script = body.find('span')
for link in script.findAll('a'):
    if re.match("/p", link.get('href')):
        links.append('https://www.instagram.com' + link.get('href'))

result = pd.DataFrame()
for i in range(len(links)):
    try:
        page = urlopen(links[i]).read()
        data = bs(page, 'html.parser')
        body = data.find('body')
        script = body.find('script')
        raw = script.text.strip().replace('window._sharedData =', '').replace(';', '')
        json_data = json.loads(raw)
        posts = json_data['entry_data']['PostPage'][0]['graphql']
        posts = json.dumps(posts)
        posts = json.loads(posts)
        x = pd.DataFrame.from_dict(json_normalize(posts), orient='columns')
        x.columns = x.columns.str.replace("shortcode_media.", "")
        result = result.append(x)

    except:
        np.nan
    # Just check for the duplicates
result = result.drop_duplicates(subset='shortcode')
result.index = range(len(result.index))
newpath = "C:/Users/Zapata/Desktop/Shopick/Users/" + username+"/"
if not os.path.exists(newpath):
    os.makedirs(newpath)
for i in range(len(result)):
    r = requests.get(result['display_url'][i])
    with open(newpath + result['shortcode'][i] + ".jpg", 'wb') as f:
        f.write(r.content)
##username = input("Enter your username")
##password = input("Enter your password")
##url = 'https://www.instagram.com/said_tezel/?hl=en'
##connection = pymysql.connect(host='localhost',
##                           user='root',
##                         password='password',
##                       db='shopick',
##                      charset='utf8mb4',
##                    cursorclass=pymysql.cursors.DictCursor)
##with connection.cursor() as cursor:
##      sql = "INSERT INTO `instagram` (`username`,`password`) VALUES (%s, %s)"
##    cursor.execute(sql, (username, password))
##connection.commit()
##connection.close()
