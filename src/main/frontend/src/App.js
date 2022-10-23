import React, { Component, useEffect } from "react";
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

function EnableToggle(){
  useEffect(() => {
        const coll = document.getElementsByClassName("collapsible");
        for (let i = 0; i < coll.length; i++) {
          coll[i].addEventListener("click", function() {
            this.classList.toggle("active");
            const content = this.nextElementSibling;
            if (content.style.display === "block") {
              content.style.display = "none";
            } else {
              content.style.display = "block";
            }
          });
        }
  });
}

class App extends Component {
  state = {
    busLines: []
  };

  async componentDidMount() {
    const response = await fetch('/v1/bus-lines/top?numberOfResults=10');
    const body = await response.json();
    this.setState({busLines: body});
  }

  render() {
    const {busLines} = this.state;
    return (
              <div id="results">
                <EnableToggle />
                <h1 className="text-center">Top 10 Bus Lines</h1>
                <div className="header">
                  <div className="busNumber">Bus Number</div>
                  <div className="stopCount">Number of Stops</div>
                </div>
                {
                  busLines.map(
                    busLine =>
                    <div className="fakeRow">
                      <button type="button" className="collapsible">
                        <div className="busNumber">{busLine.busNumber}</div>
                        <div className="stopCount">{busLine.stopCount} stops ▼</div>
                      </button>
                      <div className="content">
                        <table id="busTable" className="table table-striped">
                          <thead>
                            <tr>
                              <td>Stop Area Id</td>
                              <td>Stop Name</td>
                            </tr>
                          </thead>
                          <tbody>
                          {
                            busLine.busStops.map(
                                busStop =>
                                <tr key = {busStop.stopAreaId}>
                                     <td> {busStop.stopAreaId}</td>
                                     <td> {busStop.stopName}</td>
                                </tr>
                            )
                          }
                          </tbody>
                        </table>
                      </div>
                    </div>
                  )
                }
              </div>
          )
  }
}
export default App;