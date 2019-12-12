import matplotlib.pyplot as plt
import numpy as np
from keras.preprocessing.image import ImageDataGenerator
from keras.models import Sequential, load_model
import tensorflow as tf
import collections
import os
import dbmanagement
import scrapping

if __name__ == "__main__":
	dbhandler = dbmanagement.SQLController()
	scrapper = scrapping.scrapper()
	config = tf.ConfigProto()
	config.gpu_options.allow_growth = True
	sess = tf.Session(config=config)

	class_style = {
			0: 'Casual',
			1: 'Formal',
			2: 'Party',
			3: 'Sports',
			4: 'Vacance'}

	image_input_dir = './scraped'
	test_datagen = ImageDataGenerator(rescale=1./255)
	inpdata_generator = test_datagen.flow_from_directory(
		image_input_dir,
		target_size=(150, 150),
		batch_size=1,
		class_mode=None,
		shuffle = False)
	uID=1
	valid_user = dbhandler.search_shoppick_user(uID)
	if not len(valid_user)>0:
		quit()
	instaID, instaPW, user_ID = dbhandler.get_instaData(uID)
	scrapper.scrap(instaID,instaPW)
	user_input_dir = './scraped/'+instaID
	
	input_img_list = os.listdir(user_input_dir)
	nb_input_samples = len(input_img_list)
	c = 0
	images_input = []
	for X_batch in inpdata_generator:
		c += 1
		if (c > nb_input_samples):
			break
		images_input.append(X_batch[0, :, :, :])

	data_images_inp = np.asarray(images_input)

	style_model = load_model('./models/vgg_weights_best_style.hdf5')
	style=style_model.predict(data_images_inp)

	style_array = []	

	for i in range(len(data_images_inp)):
		print ("Style : " , class_style[np.argmax(style[i])])
		style_array.append(class_style[np.argmax(style[i])])
	
	result = collections.Counter(style_array)
	
	for item in result:
		print (item, result[item])
	a,b = result.most_common(3)[0]
	c,d = result.most_common(3)[1]
	e,f = result.most_common(3)[2]
	print(result.most_common(3))
	
	dbhandler.save_tags(user_ID, a, b)
	dbhandler.save_tags(user_ID, c, d)
	dbhandler.save_tags(user_ID, e, f)

