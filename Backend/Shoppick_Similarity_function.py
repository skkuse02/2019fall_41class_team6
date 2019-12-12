import operator
import numpy as np
from keras.preprocessing.image import ImageDataGenerator
from keras.models import Sequential, load_model
import tensorflow as tf
import dbmanagement

dbhandler = dbmanagement.SQLController()
config = tf.ConfigProto()

config.gpu_options.allow_growth = True

sess = tf.Session(config=config)

class_pattern = {
    0: 'Checked',
    1: 'Floral',
    2: 'Graphic',
    3: 'Plain',
    4: 'Striped'}
class_fabric = {
    0: 'Chiffon',
    1: 'Cotton',
    2: 'Crochet',
    3: 'Denim',
    4: 'Wool'}
class_color = {
    0: 'Black',
    1: 'Blue',
    2: 'Green',
    3: 'Red',
    4: 'White'}
class_style = {
    0: 'Casual',
    1: 'Formal',
    2: 'Party',
    3: 'Sports',
    4: 'Vacance'}

if __name__ == "__main__":

    img_width, img_height = 150, 150

    imagelist = []
    # top_model_weights_path = 'bottleneck_fc_model.h5'
    image_data_dir = 'Classification/fabric/test'
    #chosenOnes = sorted(os.listdir(train_data_dir))
    count = 0

    test_datagen = ImageDataGenerator(rescale=1./255)

    data1_generator = test_datagen.flow_from_directory(
            image_data_dir,
            target_size=(150, 150),
            batch_size=1,
            class_mode=None,
            shuffle = False)

    # Reshaping the images array
    nb_data_samples = 1293
    c=0
    images_valid=[]
    for X_batch in data1_generator:
        c+=1
        if (c>nb_data_samples):
            break
        images_valid.append(X_batch[0,:,:,:])


    data_images=np.asarray(images_valid)
    print (data_images.shape)


    image_input_dir = 'find_identical_output/'
    test_datagen = ImageDataGenerator(rescale=1./255)
    inpdata_generator = test_datagen.flow_from_directory(
            image_input_dir,
            target_size=(150, 150),
            batch_size=1,
            class_mode=None,
            shuffle = False)

    nb_input_samples = 1
    c=0
    images_input=[]
    for X_batch in inpdata_generator:
        c+=1
        if (c>nb_input_samples):
            break
        images_input.append(X_batch[0,:,:,:])


    data_images_inp=np.asarray(images_input)

    pattern_model = load_model('./models/vgg_weights_best_pattern.hdf5')
    fabric_model = load_model('./models/vgg_weights_best_fabric.hdf5')
    color_model = load_model('./models/vgg_weights_best_color.hdf5')
    style_model = load_model('./models/vgg_weights_best_style.hdf5')
    # part_model = load_model('./models/vgg_weights_best_part.hdf5')

    inp_pattern=pattern_model.predict(data_images_inp)
    inp_fabric=fabric_model.predict(data_images_inp)
    inp_color=color_model.predict(data_images_inp)
    inp_style=style_model.predict(data_images_inp)
    # part=part_model.predict(data_images_inp)

    inp_feature_list=[]
    for i in range(len(data_images_inp)):
        vals=np.zeros(4)
        vals[0] = np.argmax(inp_pattern[i])
        vals[1] = np.argmax(inp_fabric[i])
        vals[2] = np.argmax(inp_color[i])
        vals[3] = np.argmax(inp_style[i])
    #     vals[4] = np.argmax(inp_part[i])
        inp_feature_list.append(vals)

    inp_feature_data = np.asarray(inp_feature_list)

    def similarity(feature_data,inp_feature_data):
        num_samp=inp_feature_data.size
    #     print (num_samp)
        sim_score={}
        for i in range(len(feature_data)):
            score=0
    #         show_sample(data_images[i])
    #         print(feature_data[i])
            score_m= inp_feature_data - feature_data[i]
    #         print (score_m)
            score = num_samp-np.count_nonzero(score_m)
            sim_score[i]=score
    #         print (score)

        return sim_score


    feature_data=np.load('db_features.npy')
    similarities=similarity(feature_data,inp_feature_data)
    sorted_similarities = sorted(similarities.items(), key=operator.itemgetter(1),reverse=True)
    # print (sorted_similarities)
    num_reco=10
    num_data=feature_data.size
    user_ID=1
    for i in range(num_reco):
    	print("\nIndex : ", sorted_similarities[i][0])
    	clothename, style, store_url, gotostore_url = dbhandler.get_clothes_info(sorted_similarities[i][0])
    	print("Name: "+clothename+"\nStyle: "+style+"\nPic_Url: "+store_url+"\nURL: "+gotostore_url)
    	dbhandler.save_index(user_ID, sorted_similarities[i][0],clothename, style, store_url, gotostore_url)
