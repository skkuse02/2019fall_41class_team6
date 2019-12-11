from pymysql import *

class SQLController:
	def __init__(self):
		self.host='115.145.170.186'
		self.port=7040
		self.db = 'test'
		self.user = 'admin'
		self.password = '1234'
	def search_shoppick_user(self, username, password):
		try:
			self.conn = connect(host=self.host,port=self.port,db=self.db,user=self.user,password=self.password)
			self.cursor = self.conn.cursor()
			self.cursor.execute("SELECT instaID FROM user WHERE Username = %s AND Password = %s",(username,password))
			self.result = self.cursor.fetchone()[0]
			self.conn.close()
			return self.result
		except MySQLError as e:
			print('Got error {!r}, errno is {}'.format(e, e.args[0]))
	def get_instaData(self, username, password):
		try:
			self.conn = connect(host=self.host,port=self.port,db=self.db,user=self.user,password=self.password)
			self.cursor = self.conn.cursor()
			self.cursor.execute("SELECT instaID FROM user WHERE Username = %s AND Password = %s",(username,password))
			self.instaID = self.cursor.fetchone()[0]
			self.cursor.execute("SELECT instaPW FROM user WHERE Username = %s AND Password = %s",(username,password))
			self.instaPW = self.cursor.fetchone()[0]
			self.cursor.execute("SELECT user_ID FROM user WHERE Username = %s AND Password = %s",(username,password))
			self.userID = self.cursor.fetchone()[0]
			self.conn.close()
			return self.instaID, self.instaPW, self.userID
		except MySQLError as e:
			print('Got error {!r}, errno is {}'.format(e, e.args[0]))
	def save_tags(self, userID, tag, num):
		try:
			self.conn = connect(host=self.host,port=self.port,db=self.db,user=self.user,password=self.password)
			self.cursor = self.conn.cursor()
			if tag == "Casual":
				tag_num =0
			elif tag == "Formal":
				tag_num = 1
			elif tag == "Party":
				tag_num =2
			elif tag == "Sports":
				tag_num =3
			elif tag == "Vacance":
				tag_num =4
			#query = "UPDATE user_tag SET tag_num = %s  WHERE user_ID = %s"
			query = "INSERT INTO user_tag (user_ID, tag_num, num) VALUES (%s,%s,%s)"
			self.cursor.execute(query, (userID,int(tag_num),num ))
			#query2 = "UPDATE user_tag SET num = %s  WHERE user_ID = %s"
			#self.cursor.execute(query2, (int(num), userID))
			self.conn.commit()
			self.conn.close()
			print("Tags updated!")
		except MySQLError as e:
			print('Got error {!r}, errno is {}'.format(e, e.args[0]))
	def get_clothes_info(self, clothes_id):
		try:
			self.conn = connect(host=self.host,port=self.port,db=self.db,user=self.user,password=self.password)
			self.cursor = self.conn.cursor()
			self.cursor.execute("SELECT name FROM shopick_clothes WHERE clothes_id = %s",(int(clothes_id)))
			self.clothename = self.cursor.fetchone()[0]
			self.cursor.execute("SELECT style FROM shopick_clothes WHERE clothes_id = %s",(int(clothes_id)))
			self.style = self.cursor.fetchone()[0]
			self.cursor.execute("SELECT store_url FROM shopick_clothes WHERE clothes_id = %s",(int(clothes_id)))
			self.store_url = self.cursor.fetchone()[0]
			self.cursor.execute("SELECT gotostore_url FROM shopick_clothes WHERE clothes_id = %s",(int(clothes_id)))
			self.gotostore_url = self.cursor.fetchone()[0]
			self.conn.close()
			return (self.clothename, self.style, self.store_url, self.gotostore_url)
		except MySQLError as e:
			print('Got error {!r}, errno is {}'.format(e, e.args[0]))
if __name__ == "__main__":
		example = SQLController()
		example.get_instaData('123', '1')
