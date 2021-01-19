import React from 'react';
import { Divider, Container, Menu, Image} from 'semantic-ui-react';
import { RoversList, RoversImagesList } from './components'

class AppPage extends React.Component
{

  constructor(props){
    super(props);

    this.onSelectRover = this.onSelectRover.bind(this);
    this.onSelectCamera = this.onSelectCamera.bind(this);

    this.state = {
      selectedRover: null,
      selectedCamera: null
    };
  }

  onSelectRover(selectedRover){
    this.setState({selectedRover});
  }

  onSelectCamera(selectedCamera){
    this.setState({selectedCamera});
  }

  render(){
    return (
      <div>
        <Menu className="borderless fillwidth">
          <div style={{width: "250px"}}>
            <Menu.Item as='header' position="left">
              <Image src="/nasa_logo.png" style={{maxWidth: "150px", maxHeight: "64px"}}/>
            </Menu.Item>
          </div>
        </Menu>
        <Container>
          <RoversList onRoverSelected={this.onSelectRover} onCameraSelected={this.onSelectCamera}/>
          <Divider />
          <RoversImagesList rover={this.state.selectedRover} camera={this.state.selectedCamera}/>
        </Container>
      </div>
    );
  }
}

export default AppPage;

