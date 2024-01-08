import React, { useState } from 'react';
import UploadForm from './UploadForm';
import EmployeeTable from './EmployeeTable';
import SecondInterface from './SecondInterface';
import 'bootstrap/dist/css/bootstrap.min.css';

const App = () => {
  const [uploaded, setUploaded] = useState(false);
  const [processed, setProcessed] = useState(false);

  const handleUpload = () => {
    setUploaded(true);
  };

  const handleProcess = () => {
    setProcessed(true);
  };

  return (
    <div className="container">
      <h1 className="mt-5">DNA Engineering Full-Stack Assignment</h1>
      <UploadForm onUpload={handleUpload} />
      {uploaded && <SecondInterface onProcess={handleProcess} showProcessButton={!processed} />}
      {processed && <EmployeeTable />}
    </div>
  );
};

export default App;
