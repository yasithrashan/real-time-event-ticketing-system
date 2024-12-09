import './App.css';
import ConfigurationForm from './components/ConfigurationForm';
import ControlPanel from './components/ControlPanel';
import LogDisplay from './components/LogDisplay';
import TicketDisplay from './components/TicketDisplay';
import Title from './components/Title';


function App() {

  
  return (
    <div className="App">
      <Title/>
      <ConfigurationForm/>
      <ControlPanel/>
      <TicketDisplay/>
      <LogDisplay/>
      
    </div>
  );
}

export default App;
