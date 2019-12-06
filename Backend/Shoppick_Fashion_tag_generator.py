import matplotlib.pyplot as plt
import numpy as np
from keras.preprocessing.image import ImageDataGenerator
from keras.models import Sequential, load_model
import tensorflow as tf
import collections
import os

if __name__ == "__main__":

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

	user_input_dir = './scraped/user_1'
	
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

	print(result.most_common(3))

