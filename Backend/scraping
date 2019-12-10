from selenium import webdriver
from bs4 import BeautifulSoup as bs
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.chrome.options import Options
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
chrome_options = Options()
chrome_options.add_argument("--headless")
br = webdriver.Chrome(executable_path="/usr/bin/chromedriver" , options = chrome_options)
br.get('https://www.instagram.com/accounts/login/')
time.sleep(3)
#h1 = br.find_element_by_xpath("//h1[@itemprop='name']").text
#print(h1)
br.find_element_by_name('username').send_keys(username)
beforeTitle = br.title
br.find_element_by_name('password').send_keys(password, Keys.ENTER)
time.sleep(3)
afterTitle = br.title
if beforeTitle==afterTitle:
    print("Bad Login Information")
else:
    print("Login successful")
    br.get('https://www.instagram.com/'+username)
    time.sleep(5)
    Pagelength = br.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    links = []
    source = br.page_source
    data = bs(source, 'html.parser')
    body = data.find('body')
    script = body.findAll('article')[1]
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
    print(len(result.index))
    newpath = "/home/rtcl/Deep-fashion/scraped/" + username + "/"
    if not os.path.exists(newpath):
	#print("New path created")
        os.makedirs(newpath)
    for i in range(len(result)):
        r = requests.get(result['display_url'][i])
        with open(newpath + result['shortcode'][i] + ".jpg", 'wb+') as f:
            f.write(r.content)
          #connection = pymysql.connect(host='http://khsung0.dothome.co.kr/',
#                           user='khsung0',
#                         password='gmltjd1!',
#                       db='khsung0',
#                      charset='utf8mb4',
#                    cursorclass=pymysql.cursors.DictCursor)
#with connection.cursor() as cursor:
#      sql = "INSERT INTO `shopick_users` (`username`,`password`,`path`) VALUES (%s, %s, %s)"
#    cursor.execute(sql, (username, password,newpath))
#connection.commit()
#connection.close()
br.close()
br.quit()
