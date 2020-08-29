<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class makanan extends Model
{
    protected $primaryKey = 'makanan_id';
    public function pesananDetail(){
        return $this->belongsTo('App\pesananDetail','makanan_id','makanan_id');
    }
}
