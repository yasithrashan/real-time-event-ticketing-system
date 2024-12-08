import './App.css';
import ConfigurationForm from './components/ConfigurationForm';
import ControlPanel from './components/ControlPanel';
import TicketDisplay from './components/TicketDisplay';
import Title from './components/Title';


function App() {

  
  return (
    <div className="App">
      <Title/>
      <ConfigurationForm/>
      <ControlPanel/>
      <TicketDisplay/>
      
    </div>
  );
}

export default App;
