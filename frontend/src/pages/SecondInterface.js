import React from 'react';

const SecondInterface = ({ onProcess, showProcessButton }) => {
  return (
    <div className="button-container">
      {showProcessButton && <button onClick={onProcess}>Process</button>}
    </div>
  );
};

export default SecondInterface;
