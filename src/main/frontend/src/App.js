import React, { Component, useEffect } from "react";
import logo from './logo.svg';
import './App.css';
import { useTable } from 'react-table'
import 'bootstrap/dist/css/bootstrap.min.css';

function EnableToggle(){
  useEffect(() => {
        var coll = document.getElementsByClassName("collapsible");
        var i;
        for (i = 0; i < coll.length; i++) {
          coll[i].addEventListener("click", function() {
            this.classList.toggle("active");
            var content = this.nextElementSibling;
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
    const response = await fetch('/topLines?numberOfResults=10');
    const body = await response.json();
    this.setState({busLines: body});
  }

  render() {
    const {busLines} = this.state;
    return (
              <div id="results">
                <EnableToggle />
                <h1 className="text-center">Top 10 Bus Lines</h1>
                <div class="header">
                  <div class="busNumber">Bus Number</div>
                  <div class="stopCount">Number of Stops</div>
                </div>
                {
                  this.state.busLines.map(
                    busLine =>
                    <div class="fakeRow">
                      <button type="button" class="collapsible">
                        <div class="busNumber">{busLine.busNumber}</div>
                        <div class="stopCount">{busLine.stopCount} stops ▼</div>
                      </button>
                      <div class="content">
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