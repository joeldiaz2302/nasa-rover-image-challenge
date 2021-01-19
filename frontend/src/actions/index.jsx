import ResourceActions from './ResourceActions.jsx';

//Create specific action objects with generic class
export const RoverActions = (new ResourceActions("rovers", "rover")).toObject();
export const RoverImagesActions = (new ResourceActions("images")).toObject();
export const ImageUploadActions = (new ResourceActions("image_uploader")).toObject();


//export a map containing the actions. (useful for automating reducer generation)
export const resources = {
	rovers: RoverActions,
	images: RoverImagesActions,
	image_uploader: ImageUploadActions
};
