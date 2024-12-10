import './App.css';
import ConfigurationForm from './components/ConfigurationForm';
import ControlPanel from './components/ControlPanel';
import Footer from './components/Footer';
import LogDisplay from './components/LogDisplay';
import Title from './components/Title';


function App() {

  
  return (
    <div className="App">
      <Title/>
      <ConfigurationForm/>
      <ControlPanel/>
      <LogDisplay/>
      <Footer/>
      
    </div>
  );
}

export default App;
