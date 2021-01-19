import React from 'react';
import { Divider, Container, Menu, Image} from 'semantic-ui-react';
import { RoversList, RoversImagesList } from './'

class AppPage extends React.Component
{

  constructor(props){
    super(props);

    this.onSelectRover = this.onSelectRover.bind(this);

    this.state = {
      selectedRover: null
    };
  }

  onSelectRover(selectedRover){
    this.setState({selectedRover});
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
          <RoversList onRoverSelected={this.onSelectRover}/>
          <Divider />
          <RoversImagesList rover={this.state.selectedRover}/>
        </Container>
      </div>
    );
  }
}

export default AppPage;

